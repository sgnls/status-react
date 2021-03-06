(ns status-im.ui.components.common.common
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.i18n :as i18n]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.vector-icons :as vector-icons]
            [status-im.ui.components.common.styles :as styles]
            [status-im.ui.components.icons.vector-icons :as vector-icons]
            [status-im.ui.components.react :as react]
            [status-im.utils.ethereum.core :as ethereum]
            [status-im.utils.platform :as platform]
            [status-im.ui.components.icons.vector-icons :as icons]
            [status-im.ui.components.colors :as colors]))

(defn top-shadow []
  (when platform/android?
    [react/linear-gradient
     {:style  styles/gradient-bottom
      :colors styles/gradient-top-colors}]))

(defn bottom-shadow []
  (when platform/android?
    [react/linear-gradient
     {:style  styles/gradient-top
      :colors styles/gradient-bottom-colors}]))

(defn separator [style & [wrapper-style]]
  [react/view (merge styles/separator-wrapper wrapper-style)
   [react/view (merge styles/separator style)]])

(defn form-spacer []
  [react/view
   [bottom-shadow]
   [react/view styles/form-spacer]
   [top-shadow]])

(defn list-separator []
  [separator styles/list-separator])

(defn list-footer []
  [react/view styles/list-header-footer-spacing])

(defn list-header []
  [react/view styles/list-header-footer-spacing])

(defn form-title [label & [{:keys [count-value]}]]
  [react/view
   [react/view styles/form-title-container
    [react/view styles/form-title-inner-container
     [react/text {:style styles/form-title
                  :font  :medium}
      label]
     (when-not (nil? count-value)
       [react/text {:style styles/form-title-count
                    :font  :medium}
        count-value])]]
   [top-shadow]])

(defview network-info [{:keys [text-color]}]
  (letsubs [network-id [:get-network-id]]
    [react/view
     [react/view styles/network-container
      [react/view styles/network-icon
       [vector-icons/icon :icons/network {:color :white}]]
      [react/text {:style (styles/network-text text-color)}
       (if (ethereum/testnet? network-id)
         (i18n/label :t/testnet-text {:testnet (get-in ethereum/chains [(ethereum/chain-id->chain-keyword network-id) :name] "Unknown")})
         (i18n/label :t/mainnet-text))]]]))

(defn logo
  ([] (logo nil))
  ([{:keys [size icon-size shadow?] :or {shadow? true}}]
   [react/view {:style (styles/logo-container size shadow?)}
    [icons/icon :icons/logo (styles/logo icon-size)]]))

(defn bottom-button [{:keys [label disabled? on-press forward?]}]
  [react/touchable-highlight {:on-press on-press :disabled disabled?}
   [react/view (styles/bottom-button disabled?)
    [react/text {:style      styles/bottom-button-label
                 :uppercase? true}
     (or label (i18n/label :t/next))]
    (when forward?
      [icons/icon :icons/forward {:color colors/blue}])]])

(defn button [{:keys [on-press label background? style] :or {background? true}}]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style (styles/button style background?)}
    [react/text {:uppercase? true
                 :style      styles/button-label}
     label]]])

