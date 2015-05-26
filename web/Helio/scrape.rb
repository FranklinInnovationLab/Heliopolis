# encoding: utf-8

# rails runner scrape.rb com.skype.raider
@environment = ENV['RACK_ENV'] || 'development'
@dbconfig = YAML.load(File.read('config/database.yml'))
ActiveRecord::Base.establish_connection @dbconfig[@environment]
require 'net/http'
require 'uri'

def open(url)
  Net::HTTP.get(URI.parse(url))
end

package_name = ARGV[0]

if ScrapedPackage.where(package_name: package_name).take
	abort("record already in db")
end

page_content = open('https://play.google.com/store/apps/details?id=' + package_name + '&hl=en')

#get app name
page_content = page_content[page_content.index('document-title')..-1]
page_content = page_content[page_content.index('<div>')..-1]
app_name = page_content[5..page_content.index('</div>') - 1]

#get description
page_content = page_content[page_content.index('id-app-orig-desc')..-1]
description = page_content[18..page_content.index('</div>') -1]
description = description.gsub(/[^a-z0-9A-Z\s]/i, '')

puts "adding new record"
p = ScrapedPackage.new
p.name = app_name
p.package_name = package_name
p.description = description
p.save
