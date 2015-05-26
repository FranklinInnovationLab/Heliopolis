class CreateBusinesses < ActiveRecord::Migration
  def change
    create_table :businesses do |t|
      t.string :name
      t.text :address
      t.string :latitude
      t.string :longtitude

      t.timestamps null: false
    end
  end
end
