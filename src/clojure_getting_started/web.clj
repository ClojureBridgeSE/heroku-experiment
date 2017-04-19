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

(defn replace-w-unicorn [a b]
      (str a "<img src='https://thumbs.dreamstime.com/z/pooping-unicorn-smiling-rainbow-sky-39768836.jpg'/>"))

(defn multiple-unicorns [x]
  (let [x (or x 1)]
    (reduce replace-w-unicorn "" (range 0 x))))

(defn unicorns [x]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body x})

(defroutes app
  (GET "/" []
       (splash))
  (GET "/unicorns" [x]
       (unicorns x))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
