(ns fireplace-boot-example.ns2)

(defn bar-logger
  [msg]
  (.log js/console msg))

