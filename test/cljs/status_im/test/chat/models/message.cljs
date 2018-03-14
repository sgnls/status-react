(ns status-im.test.chat.models.message
  (:require [cljs.test :refer-macros [deftest is testing]]
            [status-im.chat.models.message :as m]))

(deftest add-received-message-test
  (testing "adding a new message"
    (let [old-user->clock {"a" 0 "b" 1 "c" 2}
          message-user->clock {"a" 1 "c" 1}
          new-message {:from "a"
                       :message-id "m1"
                       :chat-id 1
                       :content "message 1"
                       :user->clock message-user->clock}
          actual (m/add-message 1 new-message false {:db {:chats {1 {:user->last-clock old-user->clock
                                                                     :messages {}}}}})]
      (testing "it merges the vector clock, keeping the maximum value"
        (is (= {"a" 1 "b" 1 "c" 2}
               (get-in actual [:db :chats 1 :user->last-clock])))))))

(deftest prepare-plain-message-test
  (testing "it increments the sender public key"
    (let [old-user->clock {"a" 1
                           "b" 2}
          new-message (m/prepare-plain-message {:identity "a"}
                                               {:chat-id 1
                                                :user->last-clock old-user->clock} nil)]
      (is (= {"a" 2 "b" 2} (:user->clock new-message))))))

(deftest add-placeholder-messages
  (testing "it adds a placeholder message in place of the missing ones"
    (let [old-user->clock  {"a" 1}
          new-user->clock  {"a" 3}
          actual (m/add-placeholder-messages 1 "a" 0 old-user->clock new-user->clock {:db db})
          expected {:db {:chats {1 {:messages {"a-2" {:user->clock {"a" 2}
                                                      :message-id "a-2"
                                                      :content "Waiting for message to arrive..."
                                                      :show? true, :from "a"
                                                      :chat-id 1
                                                      :content-type "placeholder"
                                                      :timestamp 0
                                                      :outgoing false
                                                      :to "me"}}}}}}]
      (is (= expected actual)))))
