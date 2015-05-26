json.array!(@businesses) do |business|
  json.extract! business, :id, :name, :address, :latitude, :longtitude
  json.url business_url(business, format: :json)
end
