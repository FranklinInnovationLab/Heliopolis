# == Schema Information
#
# Table name: subscriptions
#
#  id             :integer          not null, primary key
#  user_id        :integer
#  business_id    :integer
#  check_in_count :integer
#  created_at     :datetime         not null
#  updated_at     :datetime         not null
#

class Subscription < ActiveRecord::Base
  
  has_one :User
  has_one :Business

  validates :user_id, presence: true
  validates :business_id, presence: true


end
