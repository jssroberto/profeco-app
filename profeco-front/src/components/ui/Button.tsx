import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'default' | 'outline';
  children: React.ReactNode;
}

const Button: React.FC<ButtonProps> = ({ 
  children, 
  variant = 'default',
  className = '', 
  ...props 
}) => {
  const baseStyles = "w-full inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none disabled:pointer-events-none disabled:opacity-50 px-4 py-2";
  
  const variants = {
    default: "bg-zinc-900 text-white hover:bg-zinc-800 cursor-pointer",
    outline: "border border-current bg-transparent hover:bg-gray-50 cursor-pointer"
  };

  const finalClassName = `${baseStyles} ${variants[variant]} ${className}`;

  return (
    <button className={finalClassName} {...props}>
      {children}
    </button>
  );
};

export default Button;