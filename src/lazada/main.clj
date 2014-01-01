(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [clojure.pprint :as pprint]
            [lazada.core :as core]
            [lazada.top :as top]
            [lazada.sub :as sub]
            [lazada.leaf :as leaf]
            [lazada.crawl :as crawl]

            [clojure.string :as string]
            ))


;; export to edn, json,

;; csv, html

;; dispatch on (map?) :name :title :leaf
;; incrementally tab sub-categories
;; strip \n whitespace
;; replace empty top5 with "empty"

(defn lzwalk [walk-fn node]

  (walk-fn node)
  (if (not (empty? (or (:children node)
                       (:content node))))
    (let [lst (if (not (empty? (:children node)))
                (:children node)
                (:content node))]
      (reduce (fn [r e]
                (lzwalk walk-fn e))
              []
              lst))))

(defn report-txt-each [file-name node]

  #_(println "")
  #_(println "processing... " node)
  (let [top-cond #(and (map? %) (:name %))
        sub-cond #(and (map? %) (:title %))
        leaf-cond #(and (map? %) (:leaf %))

        write-fn (fn [file-name tabs content]

                   (spit file-name "" :append true)
                   (let [tab "  "
                         indent (apply str (repeat tabs tab))
                         line (str indent content)]

                     (spit file-name line :append true)))

        build-list-str (fn [coll]
                         (str \newline
                              (->> coll
                                   (interpose (str ", " \newline))
                                   (map #(str "        " %))
                                   (apply str))
                              \newline))

        walk-fn (fn [inp]

                  (cond
                   (top-cond inp) (write-fn file-name 1 (str (:name inp) \newline))
                   (sub-cond inp) (write-fn file-name 2 (str (:title inp) \newline))
                   (leaf-cond inp) (do (write-fn file-name 3 (str "Name: " (:leaf inp) \newline))
                                       (write-fn file-name 3 (str "Top 5: " (if (-> inp :top5 empty?)
                                                                              (str "Empty" \newline)
                                                                              (build-list-str (:top5 inp))))))))]

    (lzwalk walk-fn node)))

(defn report-txt [file-name tree]
  (spit file-name "")
  (reduce (fn [r e] (report-txt-each file-name e))
          []
          tree))
(defn report-edn [file-name tree]
  (spit file-name (with-out-str (pprint/pprint tree))))
(defn report-json [file-name tree]
  (spit file-name (with-out-str (pprint/pprint (json/write-str tree)))))


(defn process
  ([] (process "http://lazada.com.ph"))
  ([url]
     (let [u (if url url "http://lazada.com.ph")
           tree-root (top/build-tree-root (core/get-page u))
           sub-raw (sub/build-sub-raw tree-root)
           sub-mid (sub/build-sub-mid sub-raw)
           sub-leaves (leaf/fillin-leaves sub-mid)
           crawled-leaves (crawl/crawl-leaves u sub-leaves)

           x (report-edn "lazada-categories.edn" crawled-leaves)
           x (report-json "lazada-categories.json" crawled-leaves)
           x (report-txt "lazada-categories.txt" crawled-leaves)]

       crawled-leaves)))

(defn -main [ & args ]
  (process (first args)))
