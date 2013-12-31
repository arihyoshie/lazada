(ns lazada.core
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))


(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))


;; export to edn, json, html
#_(require '[clojure.pprint :refer :all])
#_(spit "xx.edn" (with-out-str (pprint xx)))
#_(spit "xx.json" (with-out-str (pprint (json/write-str {:a 1 :b 2}))))
