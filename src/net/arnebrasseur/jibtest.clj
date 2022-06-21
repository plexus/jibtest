(ns net.arnebrasseur.jibtest
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]))

(def start-time (System/currentTimeMillis))
(def counter (atom 0))

(defn -main [& args]
  (jetty/run-jetty
   (fn [req]
     {:status 200
      :headers {"content-type" "text/plain"}
      :body (str "Hello from jibtest\n"
                 (swap! counter inc)
                 "\nUp for: " (- (System/currentTimeMillis) start-time) "ms")})
   {:port 8080
    :join? false}))
