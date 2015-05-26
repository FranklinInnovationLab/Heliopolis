json.array!(@subscriptions) do |subscription|
  json.extract! subscription, :id, :user_id, :business_id, :check_in_count
  json.url subscription_url(subscription, format: :json)
end
