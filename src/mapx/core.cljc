(ns mapx.core
  (:require [clojure.set :as set]))

(defn transform
  "Takes an input map and transforms it according to the rules
  described in xform.  xform is a map of keys :or, :select, :delete,
  :update, :rename and :project.  The transformations are applied
  specifically in this order -- keys are maybe added, selected,
  deleted, updated and renamed -- and all are optional.  :project is a
  convenience that combines the behaviour of :select and :rename; it
  selects the keys from the map and then renames as :rename does.  If
  both :select and :project are specified, the values for :select and
  keys from :project are both selected (but the :project keys then get
  renamed, of course).

  :assoc   - A map of key/value pairs to overwrite in the map.
  :or      - A map of key/value pairs to add to the map if the keys are
             missing.
  :select  - A seq of keys to select from the input map.
  :delete  - A seq of keys to delete from the input.
  :update  - A map from keys in the input map to functions to apply to
             the corresponding values using update.
  :rename  - A map from old keys to new keys.
  :project - A map that combines the behaviour of :select and :rename
             by selecting the keys of the map and then treats it as a
             rename operation."
  [m & {:as xform}]
  (cond-> m
    (contains? xform :assoc)   (merge m (:assoc xform))
    (contains? xform :or)      (merge (:or xform) m)
    (contains? xform :select)  (select-keys (concat (keys (:project xform))
                                                    (:select xform)))
    (contains? xform :delete)  (#(apply dissoc % (:delete xform)))
    (contains? xform :update)  (#(reduce (fn [m [k f]]
                                           (if (contains? m k)
                                             (update m k f)
                                             m))
                                         %
                                         (:update xform)))
    (contains? xform :rename)  (set/rename-keys (:rename xform))
    (contains? xform :project) (-> (select-keys (concat (keys (:project xform))
                                                        (:select xform)))
                                   (set/rename-keys (:project xform)))))
