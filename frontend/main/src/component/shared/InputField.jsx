const InputField = ({
    label,
    id,
    type,
    errors,
    register,
    required,
    message,
    className,
    min,
    value,
    placeholder,
}) => {
    return (
        <div className="flex flex-col gap-1.5 w-full">
            <label
                htmlFor={id}
                className={`${className ? className : ""
                    } font-semibold text-sm text-slate-700`}>
                {label}
            </label>
            <input
                type={type === "number" ? "text" : type}
                inputMode={type === "number" ? "decimal" : undefined}
                id={id}
                placeholder={placeholder}
                className={`${className ? className : ""
                    } px-3 py-2.5 border outline-none bg-white text-slate-900 rounded-lg transition-all duration-200 placeholder:text-slate-400 focus:ring-2 focus:ring-blue-900/20 ${errors[id]?.message
                        ? "border-red-500 focus:border-red-500"
                        : "border-slate-300 focus:border-blue-900"
                    }`}
                {...register(id, {
                    required: { value: required, message },
                    minLength: min
                        ? { value: min, message: `Minimum ${min} character is required` }
                        : null,
                    setValueAs:
                        type === "number"
                            ? (v) => (v === "" || v === null || v === undefined ? v : parseFloat(String(v).replace(",", ".")))
                            : undefined,
                    pattern:
                        type === "email"
                            ? {
                               // value: /^[a-zA-Z0-9]+@(?:[a-zA-Z0-9]+\.)+com+$/,
                                message: "Invalid email"
                            }
                            : type === "url"
                                ? {
                                    value: /^(https?:\/\/)?(([a-zA-Z0-9\u00a1-\uffff-]+\.)+[a-zA-Z\u00a1-\uffff]{2,})(:\d{2,5})?(\/[^\s]*)?$/,
                                    message: "Please enter a valid url"
                                }
                                : null,

                })}
            />

            {errors[id]?.message && (
                <p className="text-sm font-medium text-red-600">
                    {errors[id]?.message}
                </p>
            )}
        </div>
    );
};

export default InputField;