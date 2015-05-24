class CreateLocations < ActiveRecord::Migration
  def change
    create_table :locations do |t|
      t.float :lng
      t.float :lat
      t.integer :user_id
      t.integer :start_time, :limit => 8
      t.integer :end_time, :limit => 8

      t.timestamps null: false
    end
  end
end
