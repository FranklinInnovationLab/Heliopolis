# == Schema Information
#
# Table name: package_times
#
#  id         :integer          not null, primary key
#  start_time :integer
#  end_time   :integer
#  user_id    :integer
#  package_id :integer
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class PackageTime < ActiveRecord::Base
    belongs_to :user
    belongs_to :package
end
