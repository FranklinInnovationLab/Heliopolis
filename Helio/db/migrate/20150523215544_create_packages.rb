class CreatePackages < ActiveRecord::Migration
  def change
    create_table :packages do |t|
      t.string :package_name
      t.integer :user_id

      t.timestamps null: false
    end
  end
end
