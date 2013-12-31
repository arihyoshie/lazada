(ns lazada.crawl
  (:import [java.io.FileNotFoundException])
  (:require [clojure.walk :as walk]
            [net.cgrand.enlive-html :as html]
            [lazada.core :as core]))


(defn crawl-load [url node]
  (let [uri (:href node)
        url-full (str url uri)]

    (try (core/get-page url-full)
         (catch java.io.FileNotFoundException e
           {:error (str "ERROR: page not found: " url-full)}))))

(defn crawl-collect [url node]

  (let [crawled-page (crawl-load url node)]

    (if (:error crawled-page)

      (merge node crawled-page)

      (let [itm-links (html/select crawled-page
                                   [:#productsCatalog :li :ul.subGroupProducts :li.unit :.itm :a.itm-link])
            itm-names (map #(-> % :attrs :title)
                           itm-links)
            itm-first-five (take 5 itm-names)]

        (assoc node :top5 itm-first-five)))))

(defn crawl-leaves [url tree]
  (walk/postwalk (fn [inp]
                   (if (and (map? inp)
                            (:leaf inp))
                     (crawl-collect url inp)
                     inp))
                 tree))
