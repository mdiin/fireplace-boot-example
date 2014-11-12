(set-env!
  :src-paths #{"src/cljs"}
  :rsc-paths #{"html"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-20"]
                  ;[adzerk/boot-cljs-repl "0.1.5"]
                  [adzerk/boot-reload "0.1.3"]

                  [com.cemerick/piggieback "0.1.3"]
                  [weasel "0.4.1"]

                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/clojurescript "0.0-2371"]
                  [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                  ])

(require
  '[adzerk.boot-cljs :refer :all]
  ;'[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload :refer :all]

  '[clojure.java.io :as io]
  '[boot.pod :as pod]
  '[boot.util :refer :all]
  '[boot.core :refer :all]
  '[boot.task.built-in :refer :all]
  '[boot.from.backtick :refer [template]]
  )

(defmacro ^:private r
  [sym]
  `(do (require '~(symbol (namespace sym))) (resolve '~sym)))

(defmacro ^:private whendep
  [ns coord]
  `(when-not (do (require '~ns) :ok) '~coord))

(def ^:private ws-ip (atom nil))
(def ^:private ws-port (atom 0))
(def ^:private out-file (atom nil))
(def ^:private continue (atom nil))

(def ^:private deps
  (delay
    (concat
      (whendep cemerick.piggieback [[com.cemerick/piggieback "0.1.3"]])
      (whendep weasel.repl.websocket [[weasel "0.4.1"]])
      (whendep cljs.analyzer [[org.clojure/clojurescript "0.0-2371"]]))))

(defn piggieback-env
  [& {i :ip p :port}]
  (let [i (or i @ws-ip)
        p (or p @ws-port)
        clih (if (and i (not= i "0.0.0.0")) i "localhost")
        repl-env (->> (when i [:ip i])
                      (apply (r weasel.repl.websocket/repl-env) :port p))
        port (->> @@(r weasel.repl.server/state) :server meta :local-port)
        conn (format "ws://%s:%d" clih port)]
    (io/make-parents @out-file)
    (->> (template
           ((ns adzerk.boot-cljs-repl
              (:require [weasel.repl :as repl]))
            (when-not (repl/alive?) (repl/connect ~conn))))
         (map pr-str) (interpose "\n") (apply str) (spit @out-file))
    (touch @out-file)
    (-> (make-event) (prep-build!) (@continue))
    repl-env))

(defn start-repl
  "Start the Weasel server and attach REPL client to running browser environment.
  Keyword Options:
  :ip str The IP address the websocket server will listen on.
  :port int The port the websocket server will listen on."
  [& {i :ip p :port}]
  (let [i (or i @ws-ip)
        p (or p @ws-port)
        clih (if (and i (not= i "0.0.0.0")) i "localhost")
        mesg (with-out-str
               (->> (when i [:ip i])
                    (apply (r weasel.repl.websocket/repl-env) :port p)
                    ((r cemerick.piggieback/cljs-repl) :repl-env)))
        port (->> @@(r weasel.repl.server/state) :server meta :local-port)
        conn (format "ws://%s:%d" clih port)]
    (info (.replaceAll mesg ":[0-9]+ >>" (format ":%d >>" port)))
    (io/make-parents @out-file)
    (->> (template
           ((ns adzerk.boot-cljs-repl
              (:require [weasel.repl :as repl]))
            (when-not (repl/alive?) (repl/connect ~conn))))
         (map pr-str) (interpose "\n") (apply str) (spit @out-file))
    (touch @out-file)
    (-> (make-event) (prep-build!) (@continue))))

(deftask cljs-repl
  "Start a ClojureScript REPL server.
  The default configuration starts a websocket server on a random available
  port on localhost."
  [i ip ADDR str "The IP address for the server to listen on."
   p port PORT int "The port the websocket server listens on."]
  (let [src (mksrcdir!)]
    (when ip (reset! ws-ip ip))
    (when port (reset! ws-port port))
    (when (seq @deps) (set-env! :dependencies #(into % @deps)))
    (reset! out-file (io/file src "adzerk" "boot_cljs_repl.cljs"))
    (comp
      (repl
        :server true
        :middleware [(r cemerick.piggieback/wrap-cljs-repl)])
      (fn [continue*]
        (fn [event*]
          (reset! continue continue*)
          (continue* event*))))))

