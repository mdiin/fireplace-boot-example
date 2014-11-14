(ns fireplace-boot-example)

(enable-console-print!)

(defn foo-alerter
  [msg]
  (.alert js/window msg))

