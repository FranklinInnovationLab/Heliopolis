# == Schema Information
#
# Table name: businesses
#
#  id         :integer          not null, primary key
#  name       :string
#  address    :text
#  latitude   :string
#  longtitude :string
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class Business < ActiveRecord::Base
  
  # association macro
  has_many :users

  # validations
  validates :name, presence: true, uniqueness: true 

end
