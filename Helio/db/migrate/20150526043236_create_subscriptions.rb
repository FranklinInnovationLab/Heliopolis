class CreateSubscriptions < ActiveRecord::Migration
  def change
    create_table :subscriptions do |t|
      t.integer :user_id
      t.integer :business_id
      t.integer :check_in_count

      t.timestamps null: false
    end
  end
end
