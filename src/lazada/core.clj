(ns lazada.core
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))


;; index page
(def lindex
  (-> "http://lazada.com.ph" URL. html/html-resource))


;; get top categories
(def nodes (html/select lindex [:a.catArrow]))


;; get sibling .navLayer, for each top category
(def nl (html/select lindex [(html/left :a.catArrow)]))


;; .navLayer > .navLayerSub > .sbnc* > .bsnclco > a.bsncLink
(def blinks
  (html/select
   nl
   [:.navLayer :> :.navLayerSub :> [[:div (html/attr-starts :class "sbnc")]] :> :.bsnclco :> :a.bsncLink]))


;; find all products in each category
(def subindex
  (-> "http://www.lazada.com.ph/shop-dslr-slr" URL. html/html-resource))

(def sublinks
  (html/select subindex [:#productsCatalog :li :ul.subGroupProducts :li.unit :.itm :a.itm-link]))

(def sublink-names (map #(-> % :attrs :title)
                        sublinks))

;; record first 5 in each category
(def sublink-first-five (take 5 sublink-names))


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
