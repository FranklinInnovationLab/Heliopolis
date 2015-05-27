class BusinessesController < ApplicationController
  before_action :set_business, only: [:show, :edit, :update, :destroy]

  # GET /businesses
  # GET /businesses.json
  def index
    businesses = Business.all
    result = []
    user = User.where(:unique_id => params[:device_id])
    if user.empty?
      user = User.new(:unique_id => params[:device_id])
      user.save
    else
      user = user.first
    end
    user_id = user.id
    businesses.each do |business|
      subscribed = true
      if Subscription.where(:user_id => user_id, :business_id => business.id).empty?
        subscribed = false
      end
      result.push({:name => business.name, :id => business.id, :subscribed => subscribed })
    end
    render :json => result
  end

  # GET /businesses/1
  # GET /businesses/1.json
  # http://127.0.0.1:3000/trends?id=1
  def show
    # most common installed apps
    installed_packages = Hash.new
    Package.all.each do |package_obj|
      if installed_packages[package_obj.package_name]
        installed_packages[package_obj.package_name] += 1
      else
        installed_packages[package_obj.package_name] = 1
      end
    end
    installed_packages = installed_packages.sort_by {|_key, value| -value}

    # most used app by run time
    used_packages = Hash.new
    PackageTime.all.each do |package_obj|
      package_name = Package.where(:package_name => package_obj.package_name, :user_id => package_obj.user_id)
      diff = ((package_obj.end_time - package_obj.start_time) * 24 * 60 * 60).to_i
      if used_packages[package_name]
        used_packages[package_name] += diff
      else
        used_packages[package_name] = diff
      end
    end
    used_packages = used_packages.sort_by {|_key, value| -value}


    render :json => "not updated"
  end

  # GET /businesses/new
  def new
    @business = Business.new
  end

  # GET /businesses/1/edit
  def edit
  end

  # POST /businesses
  # POST /businesses.json
  def create
    @business = Business.new(business_params)

    respond_to do |format|
      if @business.save
        format.html { redirect_to @business, notice: 'Business was successfully created.' }
        format.json { render :show, status: :created, location: @business }
      else
        format.html { render :new }
        format.json { render json: @business.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /businesses/1
  # PATCH/PUT /businesses/1.json
  def update
    respond_to do |format|
      if @business.update(business_params)
        format.html { redirect_to @business, notice: 'Business was successfully updated.' }
        format.json { render :show, status: :ok, location: @business }
      else
        format.html { render :edit }
        format.json { render json: @business.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /businesses/1
  # DELETE /businesses/1.json
  def destroy
    @business.destroy
    respond_to do |format|
      format.html { redirect_to businesses_url, notice: 'Business was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_business
      # @business = Business.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def business_params
      # params.require(:business).permit(:name, :address, :latitude, :longtitude)
    end
end
