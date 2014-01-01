(ns lazada.report
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]
            [clojure.data.json :as json]))


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
