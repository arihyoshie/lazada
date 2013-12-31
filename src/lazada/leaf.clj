(ns lazada.leaf
  (:require [net.cgrand.enlive-html :as html]))


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
