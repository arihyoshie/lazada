(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))


;; ===
(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))


;; ===
(defn get-top-siblings [page]
  (->> (html/select page [(html/left :a.catArrow)])
       (remove #(= % "\n"))))

(defn get-top-category-names [nodes]
  (map html/text
       (html/select nodes [:a.catArrow :> :span.navSubTxt])))

(defn get-top-categories [page]
  (html/select page [:li.multiMenu]))


(defn build-tree-root [url]
  (let [top-page (get-page url)
        top-categories (get-top-categories top-page)

        name-fn #(get-top-category-names %)
        children-fn #(get-top-siblings %)]

    (map (fn [inp]
           {:name (first (name-fn inp))
            :children (children-fn inp)})
         top-categories)))

(defn -main [ & args ]

  (let [
        ;; top-page (get-page "http://lazada.com.ph")
        ;; top-categories (get-top-categories top-page)
        ;; top-category-names (get-top-category-names top-categories)
        ;; top-siblings (get-top-siblings top-categories)

        tree-root (build-tree-root "http://lazada.com.ph")

        ;; ... foreach .navLayer, get sub-category (.bsnch, .bsnclco) **structure**

        ;; ... foreach sub-category, get target-category (.bsncLink) **structure**

        ;; ====

        ;; ... foreach target-category, crawl and get each sub-page

        ;; ... get sub-links and names **structure**

        ;; ... get first 5 names **structure**

        ]

    ))
