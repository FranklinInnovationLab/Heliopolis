class CreateScrapedPackages < ActiveRecord::Migration
  def change
    create_table :scraped_packages do |t|
      t.string :package_name
      t.string :name
      t.string :description

      t.timestamps null: false
    end
  end
end
