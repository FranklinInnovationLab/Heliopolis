# == Schema Information
#
# Table name: locations
#
#  id         :integer          not null, primary key
#  lng        :float
#  lat        :float
#  user_id    :integer
#  start_time :integer
#  end_time   :integer
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

require 'test_helper'

class LocationTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
end
