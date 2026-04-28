/**
 * @param dialog {HTMLDialogElement}
 * @param url {string}
 * @param onSuccess {() => void}
 */
export function initializeDialog(dialog, url, onSuccess = () => {})
{
    const action = dialog.dataset.type;

    dialog.querySelector("button.secondary").addEventListener("click", () => dialog.close("No"));
    dialog.querySelector("button:not(.secondary)").addEventListener("click", () => dialog.close("Yes"));

    const targets = document.querySelectorAll(`[data-action="${CSS.escape(action)}"]`);
    for (const target of targets)
    {
        target.addEventListener("click", (event) =>
        {
            event.preventDefault();

            for (const key of Object.keys(target.dataset))
            {
                const placeholder = dialog.querySelector(`[data-key="${CSS.escape(key)}"]`);
                if (placeholder) { placeholder.textContent = target.dataset[key]; }
            }

            dialog.addEventListener("close", async () =>
            {
                if (dialog.returnValue === "Yes")
                {
                    const body = toSearchParams(dialog);
                    console.log(body);
                    const response = await fetch(template(url, target.dataset), { method: "POST", body });
                    if (response.ok) { onSuccess(); }
                }
            }, { once: true });
            dialog.showModal();
        });
    }
}

/**
 * @param container {Element}
 * @return {URLSearchParams}
 */
function toSearchParams(container)
{
    const params = new URLSearchParams();

    /** @type {NodeListOf<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>} */
    const fields = container.querySelectorAll("input, select, textarea");
    for (const field of fields)
    {
        const name = field.name;

        if (field.matches("input[type=checkbox]"))
        {
            if (field.checked) { params.append(name, field.value); }
        }
        else if (field.matches("input[type=radio]"))
        {
            if (field.checked) { params.set(name, field.value); }
        }
        else if (field.matches("select[multiple]"))
        {
            for (const option of field.selectedOptions)
            {
                params.append(name, option.value);
            }
        }
        else
        {
            params.append(name, field.value);
        }
    }

    return params;
}

/**
 * @param input {string}
 * @param params {Record<string, string>}
 * @return {string}
 */
function template(input, params)
{
    return input.replaceAll(/\{(\w+)}/g, (_, name) => params[name]);
}