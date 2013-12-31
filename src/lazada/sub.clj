(ns lazada.sub
  (:require [net.cgrand.enlive-html :as html]))


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

(defn build-sub-mid [tree]
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
