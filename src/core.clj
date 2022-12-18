(ns core
  (:require [org.httpkit.server :as srv]
            [hiccup2.core :refer [html]]))

(defn extract-instance-name [user-agent-string]
  (println user-agent-string)
  (let [[_ version host] (re-find #"\((.*).?\;.?\+https:\/\/(.*)\/\)" user-agent-string)]
    (if (and version (.contains version "Mastodon"))
      host
      nil)))

(defn extract-client-info [{:keys [headers]}]
  (extract-instance-name (get headers "user-agent")))

(defn handle-request [request]
  (let [host (extract-client-info request)]
    (if host
      (let [bait (str "Users of Mastodon instance " host " are the smartest!")]
        {:headers {"content-type" "text/html"}
         :body
         (str (html [:html
                     [:head [:title bait]]
                     [:body [:h1 bait]
                      [:p (str "This is unbelievable! A recent study discovered, that " bait)]]]))})
      {:status 301
       :headers {"Location" "https://javahippie.net"}})))

(srv/run-server #'handle-request
                {:port 80})

(while true)

(comment
  (handle-request
    {:headers {"user-agent" "http.rb/5.1.0 (Mastodon/4.0.2; +https://freiburg.social/) Bot"}})
  )