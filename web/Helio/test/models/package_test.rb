# == Schema Information
#
# Table name: packages
#
#  id           :integer          not null, primary key
#  package_name :string
#  user_id      :integer
#  created_at   :datetime         not null
#  updated_at   :datetime         not null
#

require 'test_helper'

class PackageTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
end
