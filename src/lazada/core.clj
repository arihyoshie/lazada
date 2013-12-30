(ns lazada.core
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]))


;; index page
(def lindex
  (-> "http://lazada.com.ph" URL. html/html-resource))


;; get top categories
(def nodes (html/select lindex [:a.catArrow]))


;; get sibling .navLayer, for each top category
(def ff (first nodes))
(def nl (html/select lindex [(html/left :a.catArrow)]))


;; .navLayer > .navLayerSub > .sbnc* > .bsnclco > a.bsncLink
(def blinks
  (html/select
   nl
   [:.navLayer :> :.navLayerSub :> [[:div (html/attr-starts :class "sbnc")]] :> :.bsnclco :> :a.bsncLink]))


;; crawl lazada.com.ph. recursively pull in all pages, record
;;   i. top category (.catArrow)
;;   ii. sub-category (.bsnch, .bsnclco)
;;   iii. target category (.bsncLink)

;; export to edn, json, csv, html



;; find all products in each category
(def subindex
  (-> "http://www.lazada.com.ph/shop-dslr-slr" URL. html/html-resource))

(def sublinks
  (html/select subindex [:#productsCatalog :li :ul.subGroupProducts :li.unit :.itm :a.itm-link]))

(def sublink-names (map #(-> % :attrs :title)
                        core/sublinks))

;; record first 5 in each category
(def sublink-first-five (take 5 sublink-names))


;; A) Get index page
;; B) Build category tree
;; C) Crawl sub-pages
;; D) ...
