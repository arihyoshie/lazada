(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [lazada.top :as top]))


(defn list-fns-in-ns [input-ns]
  (require input-ns)
  (keys (ns-publics input-ns)))


;; ===
(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))


;; ===
(defn get-sub-categories [nodes]

  (let [sub-categories (html/select
                        nodes
                        [:.navLayer :> :.navLayerSub :> [[:div (html/attr-starts :class "sbnc")]]])

        ;; discard if there's an .sbncco (not a category)
        highlight-check #(> 0
                            (count (html/select % [:.sbncco])))]

    (remove highlight-check sub-categories)))


(defn build-sub-node [node]

  (let [children (:children node)]

    (if (nil? children)
      node
      (let [;; category title & contents immediately below it
            cat-title  (map (fn [inp] {:title (html/text inp)})
                            (html/select children [:.bsnch]))

            cat-contents (map (fn [inp] {:content inp})
                              (html/select children [(html/left :.bsnch)]))

            paired-cats (map #(merge (first %) (second %))
                             (seq (zipmap cat-title cat-contents)))]

        (assoc node :children paired-cats)))))

(defn build-sub-tree [tree]
  (reduce (fn [rslt ech]
            (let [new-child (build-sub-node ech)]
              (conj rslt new-child)))
            []
            tree))


(defn build-sub-raw [tree]
  (let [getsub-fn (fn [inp]
                    (assoc inp
                      :children
                      (get-sub-categories (:children inp))))]

    (map getsub-fn tree)))

#_(def blinks
  (html/select
   nl
   [:.navLayer :> :.navLayerSub :> [[:div (html/attr-starts :class "sbnc")]] :> :.bsnclco :> :a.bsncLink]))

;; ===
(defn -main [ & args ]

  (let [tree-root (top/build-tree-root (get-page "http://lazada.com.ph"))
        sub-raw (build-sub-raw tree-root)
        sub-tree (build-sub-tree sub-raw)

        ;; ... foreach .navLayer, get sub-category (.bsnch, .bsnclco) **structure**

        ;; ... foreach sub-category, get target-category (.bsncLink) **structure**

        ;; ====

        ;; ... foreach target-category, crawl and get each sub-page

        ;; ... get sub-links and names **structure**

        ;; ... get first 5 names **structure**

        ]

    ))
