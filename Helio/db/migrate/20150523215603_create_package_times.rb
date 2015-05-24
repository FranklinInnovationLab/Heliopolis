class CreatePackageTimes < ActiveRecord::Migration
  def change
    create_table :package_times do |t|
      t.integer :start_time, :limit => 8
      t.integer :end_time, :limit => 8
      t.integer :user_id
      t.integer :package_id

      t.timestamps null: false
    end
  end
end
