(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Heroku"})

(defn unicorns []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<img src='https://thumbs.dreamstime.com/z/pooping-unicorn-smiling-rainbow-sky-39768836.jpg'/>"})

(defroutes app
  (GET "/" []
       (splash))
  (GET "/unicorns" []
       (unicorns))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
