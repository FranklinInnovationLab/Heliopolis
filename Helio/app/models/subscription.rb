class Subscription < ActiveRecord::Base
  
  has_one :User
  has_one :Business

  validates :user_id, prescense: true
  validates :business_id, prescense: true


end
