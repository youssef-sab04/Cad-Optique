import { NavLink } from "react-router-dom";

const SidebarContent = ({ sections, collapsed, onNavigate }) => (
  <nav className="flex flex-1 flex-col gap-6 overflow-y-auto px-3 py-4">
    {sections.map((section) => (
      <div key={section.label}>
        {!collapsed && (
          <p className="mb-2 px-3 font-body text-[11px] font-semibold uppercase tracking-wider text-slate-500">
            {section.label}
          </p>
        )}
        <div className="flex flex-col gap-1">
          {section.links.map(({ label, icon: Icon, path }) => (
            <NavLink
              key={path}
              to={path}
              end={path === "/dashboard"}
              onClick={onNavigate}
              title={collapsed ? label : undefined}
              className={({ isActive }) =>
                `group relative flex items-center gap-3 rounded-lg px-3 py-2.5 font-body text-sm font-medium transition-all duration-200 ${
                  collapsed ? "justify-center" : ""
                } ${
                  isActive
                    ? "bg-white/10 text-white"
                    : "text-slate-400 hover:bg-white/5 hover:text-white"
                }`
              }
            >
              {({ isActive }) => (
                <>
                  <span
                    className={`absolute left-0 top-1/2 h-5 w-[3px] -translate-y-1/2 rounded-r-full bg-blue-500 transition-all duration-200 ${
                      isActive ? "opacity-100" : "opacity-0"
                    }`}
                  />
                  <Icon
                    className={`h-[18px] w-[18px] shrink-0 transition-colors duration-200 ${
                      isActive ? "text-blue-400" : "text-slate-500 group-hover:text-blue-400"
                    }`}
                  />
                  {!collapsed && label}
                </>
              )}
            </NavLink>
          ))}
        </div>
      </div>
    ))}
  </nav>
);

export default SidebarContent