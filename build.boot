(set-env!
  :src-paths #{"src/cljs"}
  :rsc-paths #{"html"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-20"]
                  [adzerk/boot-cljs-repl "0.1.5"]
                  [adzerk/boot-reload "0.1.3"]

                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/clojurescript "0.0-2371"]
                  [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                  ])

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload :refer :all])

