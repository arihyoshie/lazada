(ns lazada.core
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))


(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))


;; find all products in each category
#_(def subindex
  (-> "http://www.lazada.com.ph/shop-dslr-slr" URL. html/html-resource))

#_(def sublinks
  (html/select subindex [:#productsCatalog :li :ul.subGroupProducts :li.unit :.itm :a.itm-link]))

#_(def sublink-names (map #(-> % :attrs :title)
                        sublinks))

;; record first 5 in each category
#_(def sublink-first-five (take 5 sublink-names))


;; export to edn, json, html
#_(require '[clojure.pprint :refer :all])
#_(spit "xx.edn" (with-out-str (pprint xx)))
#_(spit "xx.json" (with-out-str (pprint (json/write-str {:a 1 :b 2}))))



;; crawl lazada.com.ph. recursively pull in all pages, record
;;   i. top category (.catArrow)
;;   ii. sub-category (.bsnch, .bsnclco) ** discard if there's an .sbncco (not a category)
;;   iii. target category (.bsncLink)


;; A) Get index page
;; B) Build category tree
;; C) Crawl sub-pages
;; D) ...
