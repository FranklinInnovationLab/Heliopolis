class Business < ActiveRecord::Base
  
  # association macro
  has_many :users

  # validations
  validates :name, prescense: true, unqiueness: true

end
