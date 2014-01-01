(ns lazada.core
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))


(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))
