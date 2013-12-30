(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))




(defn get-top-siblings [page]
  (html/select page [(html/left :a.catArrow)]))

(defn get-top-category-names [nodes]
  (map html/text
       (html/select nodes [:a.catArrow :> :span.navSubTxt])))

(defn get-top-categories [page]
  (html/select page [:a.catArrow]))

(defn get-page [purl]
  (-> purl URL. html/html-resource))


(defn -main [ & args ]

  (let [top-page (get-page "http://lazada.com.ph")
        top-categories (get-top-categories top-page)
        top-category-names (get-top-category-names top-categories)
        top-siblings (html/select top-page [(html/left :a.catArrow)])

        ;; ... interleave names and siblings **structure**

        ;; ... foreach .navLayer, get sub-category (.bsnch, .bsnclco) **structure**

        ;; ... foreach sub-category, get target-category (.bsncLink) **structure**

        ;; ====

        ;; ... foreach target-category, crawl and get each sub-page

        ;; ... get sub-links and names **structure**

        ;; ... get first 5 names **structure**

        ]

    ))
