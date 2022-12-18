(ns core
  (:require [org.httpkit.server :as srv]
            [hiccup2.core :refer [html]])
  (:import (java.time LocalDateTime)))

(defn logged [user-agent-string]
  (spit "user-agents.log" (str user-agent-string ";" (.toString (LocalDateTime/now)) "\n") :append true)
  user-agent-string)

(defn extract-instance-name [user-agent-string]
  (let [[_ version host] (re-find #"\((.*).?\;.?\+https:\/\/(.*)\/\)" user-agent-string)]
    (if (and version (.contains version "Mastodon"))
      host
      nil)))

(defn extract-client-info [{:keys [headers]}]
  (extract-instance-name (logged (get headers "user-agent"))))

(defn handle-request [request]
  (let [host (extract-client-info request)]
    (if host
      (let [bait (str "Why " host " is the best Mastodon instance to be on!")]
        {:headers {"content-type" "text/html"}
         :body
         (str (html [:html
                     [:head [:title bait]]
                     [:body [:h1 bait]]]))})
      {:status 301
       :headers {"Location" "https://javahippie.net"}})))

(srv/run-server #'handle-request
                {:port 80})

(while true)

(comment
  (handle-request
    {:headers {"user-agent" "http.rb/5.1.0 (Mastodon/4.0.2; +https://freiburg.social/) Bot"}})
  )