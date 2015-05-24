class User < ActiveRecord::Base
    has_many :locations
    has_many :package_times
    has_many :packages
end
