class ApplicationController < ActionController::Base
    # Prevent CSRF attacks by raising an exception.
    # For APIs, you may want to use :null_session instead.
    protect_from_forgery with: :null_session
    
    def update
        @current_user = update_user
        update_installed_apps
        update_used_apps
        update_locations
        render :json => "updated"
    end
    
    def dump_package_times
        render :json => PackageTime.all
    end
    
    def dump_packages
        render :json => Package.all
    end
    
    def dump_users
        render :json => User.all
    end
    
    def dump_locations
        render :json => Location.all
    end
    
    def update_user
        unique_id = params[:id]
        query = User.where(:unique_id => unique_id)
        if not query.empty?
            query.first
        else
            User.create(:unique_id => unique_id)
        end
    end
    
    def update_installed_apps
        installed_apps = params[:installed_apps]
        if installed_apps
            installed_apps.each do |app|
                update_app app
            end
        end
    end
    
    def update_app(app)
        query = Package.where(:package_name => app, :user => @current_user)
        if query.empty?
            Package.create(:package_name => app, :user => @current_user)
        end
    end
    
    def update_used_apps
        used_apps = params[:used_apps]
        if used_apps
            used_apps.keys.each do |key|
                intervals = used_apps[key]
                package = Package.where(:package_name => key, :user => @current_user).first
                process_intervals package, intervals
            end
        end
    end
    
    def process_intervals(package, intervals)
        intervals.each do |interval|
            PackageTime.create(:package => package, :start_time => interval.first, :end_time => interval.last, :user => @current_user)
        end
    end
    
    def update_locations
        locations = params[:locations]
        if locations
            locations.each do |location|
                Location.create(:user => @current_user, :lng => location[0], :lat => location[1], :start_time => location[2], :end_time => location[3])
            end
        end
    end
end
