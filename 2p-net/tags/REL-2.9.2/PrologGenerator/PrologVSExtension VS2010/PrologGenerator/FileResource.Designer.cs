﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.544
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace PrologGenerator {
    using System;
    
    
    /// <summary>
    ///   A strongly-typed resource class, for looking up localized strings, etc.
    /// </summary>
    // This class was auto-generated by the StronglyTypedResourceBuilder
    // class via a tool like ResGen or Visual Studio.
    // To add or remove a member, edit your .ResX file then rerun ResGen
    // with the /str option, or rebuild your VS project.
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")]
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    internal class FileResource {
        
        private static global::System.Resources.ResourceManager resourceMan;
        
        private static global::System.Globalization.CultureInfo resourceCulture;
        
        [global::System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1811:AvoidUncalledPrivateCode")]
        internal FileResource() {
        }
        
        /// <summary>
        ///   Returns the cached ResourceManager instance used by this class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Resources.ResourceManager ResourceManager {
            get {
                if (object.ReferenceEquals(resourceMan, null)) {
                    global::System.Resources.ResourceManager temp = new global::System.Resources.ResourceManager("PrologGenerator.FileResource", typeof(FileResource).Assembly);
                    resourceMan = temp;
                }
                return resourceMan;
            }
        }
        
        /// <summary>
        ///   Overrides the current thread's CurrentUICulture property for all
        ///   resource lookups using this strongly typed resource class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Globalization.CultureInfo Culture {
            get {
                return resourceCulture;
            }
            set {
                resourceCulture = value;
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to using System;
        ///using System.Collections.Generic;
        ///using System.Linq;
        ///using System.Text;
        ///using alice.tuprolog;
        ///using java.io;
        ///
        ///namespace #NameSpace {
        ///        ///	 public class #ClassName : Prolog {        /// 
        ///			 public #ClassName(){ 
        ///					 setTheory(new Theory(new FileInputStream(&quot;#ExtFile&quot;))); 
        ///		} 
        ///
        ///	 } 
        ///        ///}.
        /// </summary>
        internal static string CSharpExternalTemplate {
            get {
                return ResourceManager.GetString("CSharpExternalTemplate", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to using System;
        ///using System.Collections.Generic;
        ///using System.Linq;
        ///using System.Text;
        ///using alice.tuprolog;
        ///
        ///namespace #NameSpace {
        ///        ///	 public class #ClassName : Prolog {
        ///	  
        ///	 private string th = @&quot;
        ///	 #Theory&quot;; 
        ///
        ///		 public #ClassName(){ 
        ///					 setTheory(new Theory(th)); 
        ///		} 
        ///	 }         ///}.
        /// </summary>
        internal static string CSharpTemplate {
            get {
                return ResourceManager.GetString("CSharpTemplate", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to Imports alice.tuprolog
        ///Module #NameSpace
        ///
        ///	Public Class #ClassName
        ///		Inherits Prolog
        ///
        ///		Public Sub New()
        ///			Me.setTheory(New Theory(New FileInputStream(&quot;#ExtFile&quot;)))
        ///
        ///		End Sub
        ///
        ///	End Class
        ///End Module
        ///
        ///namespace #NameSpace {
        ///        ///	 public class #ClassName : Prolog {        /// 
        ///			 public #ClassName(){ 
        ///					 setTheory(new Theory(new FileInputStream(&quot;#ExtFile&quot;))); 
        ///		} 
        ///
        ///	 } 
        ///        ///}.
        /// </summary>
        internal static string VBExternalTemplate {
            get {
                return ResourceManager.GetString("VBExternalTemplate", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to Imports alice.tuprolog
        ///Module #NameSpace
        ///
        ///    Public Class #ClassName
        ///        Inherits Prolog
        ///        Dim th As String = &quot;#theory&quot;
        ///
        ///        Public Sub New()
        ///            Me.setTheory(New Theory(th))
        ///
        ///        End Sub
        ///
        ///    End Class
        ///End Module
        ///.
        /// </summary>
        internal static string VBTemplate {
            get {
                return ResourceManager.GetString("VBTemplate", resourceCulture);
            }
        }
    }
}
