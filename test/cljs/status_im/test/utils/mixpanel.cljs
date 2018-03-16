(ns status-im.test.utils.mixpanel
  (:require [cljs.test :refer-macros [deftest is testing async]]
            [status-im.utils.mixpanel :as mixpanel]))

(deftest events
  (is (not (nil? mixpanel/events))))

(deftest matching-event
  (is (nil? (mixpanel/matching-event [:non-existing])))
  (let [{:keys [label]} (mixpanel/matching-event [:navigate-to-tab])]
    (is (not (nil? label)))))
