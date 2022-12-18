(ns core
  (:require [org.httpkit.server :as srv]))

(defn extract-client-info [{:keys [headers]}]
  {:user-agent (get headers "user-agent")
   :host (get headers "host")})

(defn handle-request [request]
  {:body
   (str (extract-client-info request))})

(comment
  (srv/run-server #'handle-request
                  {:port 3000}))

