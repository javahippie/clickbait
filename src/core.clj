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
  (let [host (extract-client-info request)
        bait (if host
               (str "Users of Mastodon instance " host " are the smartest!")
               (str "Mastodon users are the smartest!"))]
    {:headers {"content-type" "text/html"}
     :body
     (str (html [:html
                 [:head [:title bait]]
                 [:body [:h1 bait]
                  [:p (str "This is unbelievable! A recent study discovered, that " bait)]
                  [:p "Unfortunately, this is not true. This small page just makes use of the fact, that every Mastodon instance creates its own Link preview, and sends its URL along in the user agent."]
                  [:p "This was brought to you by " [:a {:href "https://freiburg.social/@javahippie"} "Tim"] " and a simple " [:a {:href "https://github.com/javahippie/clickbait"} "script"]]]]))}))

(srv/run-server #'handle-request
                {:port 80})

#_(while true)

(comment
  (handle-request
    {:headers {"user-agent" "http.rb/5.1.0 (Mastodon/4.0.2; +https://freiburg.social/) Bot"}})
  )