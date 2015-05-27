class SubscriptionsController < ApplicationController
  before_action :set_subscription, only: [:show, :edit, :update, :destroy]

  # GET /subscriptions
  # GET /subscriptions.json
  def index
    @subscriptions = Subscription.all
  end

  # GET /subscriptions/1
  # GET /subscriptions/1.json
  def show
  end

  # GET /subscriptions/new
  def new
    @subscription = Subscription.new
  end

  # GET /subscriptions/1/edit
  def edit
  end

  # POST /subscriptions
  # POST /subscriptions.json
  # 127.0.0.1:3000/subscriptions/update
  def toggle
    # put user_id business_id in params
    user = User.where(:unique_id => params[:device_id])
    if user.empty?
      user = User.new(:unique_id => params[:device_id])
      user.save
    else
      user = user.first #WHERE RETURNS AN ARRAY
    end
    user_id = user.id
    @subscription = Subscription.where(:business_id => params[:business_id], :user_id => user_id)
    if @subscription.empty?
      @subscription = Subscription.new(:business_id => params[:business_id], :user_id => user_id)
      @subscription.save
      render :json => "subscribed"
    else
      @subscription.first.destroy
      render :json => "unsubscribed"
    end
  end

  # DELETE /subscriptions/1
  # DELETE /subscriptions/1.json
  def destroy
    @subscription.destroy
    respond_to do |format|
      format.html { redirect_to subscriptions_url, notice: 'Subscription was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def subscription_params
    params.permit(:user_id, :business_id)
  end
end
