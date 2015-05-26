# == Schema Information
#
# Table name: users
#
#  id         :integer          not null, primary key
#  unique_id  :string
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class User < ActiveRecord::Base
    has_many :locations
    has_many :package_times
    has_many :packages
    has_many :businesses
end
