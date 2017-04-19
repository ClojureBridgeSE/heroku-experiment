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

(defn image-unicorn [x]
      (let [w (float (/ 100 x))]
        (str "<img src='https://thumbs.dreamstime.com/z/pooping-unicorn-smiling-rainbow-sky-39768836.jpg' width='"w"%' />")))

(defn multiple-unicorns [x]
  (let [x (Integer/parseInt (or x "1"))]
    (apply str (for [i (range 0 x)]
                 (image-unicorn x)))))

(defn unicorns [x]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (multiple-unicorns x)})

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
