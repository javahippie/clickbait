(ns core
  (:require [org.httpkit.server :as srv]
            [hiccup2.core :refer [html]]))

(defn extract-instance-name [user-agent-string]
  (let [[_ version host] (re-find #"\((.*) \; \+https:\/\/(.*)\/\)" user-agent-string)]
    (if (and version (.contains version "Mastodon"))
      {:version version
       :host    host}
      {})))

(defn extract-client-info [{:keys [headers]}]
  (extract-instance-name (get headers "user-agent")))

(defn handle-request [request]
  (let [{:keys [version host]} (extract-client-info request)]
    {:body
     (html [:html
            [:head [:title "Yeah!"]]
            [:body [:h1 host]]])}))

(srv/run-server #'handle-request
                {:port 80})

(while true
  )





(comment
  (handle-request
    {:headers {"user-agent" "http.rb /5.1.0 (Mastodon /4.0.2 ; +https://freiburg.social/) Bot"}})
  )