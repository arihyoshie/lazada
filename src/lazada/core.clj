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
