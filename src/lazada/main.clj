(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [lazada.top :as top]
            [lazada.sub :as sub]))


(defn list-fns-in-ns [input-ns]
  (require input-ns)
  (keys (ns-publics input-ns)))


;; ===
(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))



(defn fillin-leaf [node]

  (let [content (-> node :content)
        as (html/select content [:.bsnclco :> :a.bsncLink])
        am (map (fn [inp]
                  {:leaf (-> inp :content first)
                   :href (-> inp :attrs :href)})
                as)]

    (assoc node :content am)))

(defn fillin-leaves [tree]

  (let [leaf-fn (fn [r1 e1]
                  (conj r1 (fillin-leaf e1)))]

    (reduce (fn [r2 e2]
              (let [kids (:children e2)
                    leaf-kids (reduce leaf-fn [] kids)]

                (conj r2 (assoc e2 :children leaf-kids))))
            []
            tree)))


#_(def four (nth sub-mid 3))
#_(def blinks
  (html/select
   nl
   [:.navLayer :> :.navLayerSub :> [[:div (html/attr-starts :class "sbnc")]] :> :.bsnclco :> :a.bsncLink]))


;; ===
(defn -main [ & args ]

  (let [tree-root (top/build-tree-root (get-page "http://lazada.com.ph"))
        sub-raw (sub/build-sub-raw tree-root)
        sub-mid (sub/build-sub-mid sub-raw)
        sub-leaves (fillin-leaves sub-mid)

        ;; ... foreach .navLayer, get sub-category (.bsnch, .bsnclco) **structure**

        ;; ... foreach sub-category, get target-category (.bsncLink) **structure**

        ;; ====

        ;; ... foreach target-category, crawl and get each sub-page

        ;; ... get sub-links and names **structure**

        ;; ... get first 5 names **structure**

        ]

    ))
